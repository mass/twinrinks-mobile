//
//  ScheduleTodayViewController.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/8/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "ScheduleGamesViewController.h"
#import "Game.h"
#import "MemoryManager.h"

@interface ScheduleGamesViewController ()

@end

@implementation ScheduleGamesViewController

@synthesize gameArray;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    MemoryManager *myManager = [[MemoryManager alloc] init];
    gameArray = [myManager getGameArray];
    
    if([self.dataToDisplay isEqualToString:@"AllGames"]){}
        //Do Nothing
    else if([self.dataToDisplay isEqualToString:@"Today"])
        [self trimGameArrayForToday];
    else if([self.dataToDisplay isEqualToString:@"Playoffs"])
        [self trimGameArrayForPlayoffs];
    else {
        NSArray *data = [self.dataToDisplay componentsSeparatedByString:@","];
        [self trimGameArrayForTeam:[((NSString *) [data objectAtIndex:1]) stringByReplacingOccurrencesOfString:@";" withString:@""] andLeague:[data objectAtIndex:0]];
    }
    [self.tableView reloadData];
}

-(void)trimGameArrayForTeam:nameP andLeague:leagueP {
    NSMutableArray *tempArray = [NSMutableArray arrayWithArray:gameArray];
    for(int i=tempArray.count-1;i>=0;i--) {
        Game *temp = [tempArray objectAtIndex:i];
        if((![temp.league isEqualToString:leagueP]) || (!([temp.homeTeam isEqualToString:nameP] || [temp.awayTeam isEqualToString:nameP]))) {
            [tempArray removeObjectAtIndex:i];
        }
    }
    
    gameArray = [NSArray arrayWithArray:tempArray];
}

-(void)trimGameArrayForToday {
    NSMutableArray *tempArray = [NSMutableArray arrayWithArray:gameArray];
    
    NSDateFormatter *format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"MM/dd/yy"];
    NSString *todayString = [format stringFromDate:[NSDate date]];
    
    for(int i=0;i<tempArray.count;i++) {
        if(![((NSString *)(((Game *)[tempArray objectAtIndex:i]).date))isEqualToString:todayString]) {
            [tempArray removeObjectAtIndex:i];
            i--;
        }
    }
    
    gameArray = [NSArray arrayWithArray:tempArray];
}

-(void)trimGameArrayForPlayoffs {
    NSMutableArray *tempArray = [NSMutableArray arrayWithArray:gameArray];
    
    for(int i=tempArray.count-1;i>=0;i--) {
        if(![((Game *)[tempArray objectAtIndex:i]).homeTeam isEqualToString:@"PLAYOFFS"]) {
            [tempArray removeObjectAtIndex:i];
        }
    }
    
    gameArray = [NSArray arrayWithArray:tempArray];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return gameArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"scheduleCell";
    UITableViewCell *cell;
    cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    
    UILabel *homeLabel = (UILabel *)[cell viewWithTag:1];
    UILabel *awayLabel = (UILabel *)[cell viewWithTag:2];
    UILabel *dayDateTimeLabel = (UILabel *)[cell viewWithTag:3];
    UILabel *leagueLabel = (UILabel *)[cell viewWithTag:4];
    UILabel *rinkLabel = (UILabel *)[cell viewWithTag:5];
    
    Game *temp = ((Game *)[gameArray objectAtIndex:indexPath.row]);
    
    homeLabel.text =  temp.homeTeam;
    awayLabel.text = temp.awayTeam;
    leagueLabel.text = temp.league;
    rinkLabel.text = temp.rink;
    dayDateTimeLabel.text = [NSString stringWithFormat:@"%@ on %@, at %@",temp.day,temp.date,temp.startTime];
    return cell;
}

@end
