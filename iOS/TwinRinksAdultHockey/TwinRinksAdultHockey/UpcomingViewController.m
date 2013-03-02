//
//  UpcomingViewController.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/6/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "UpcomingViewController.h"
#import "Game.h"
#import "Team.h"
#import "MemoryManager.h"

@implementation UpcomingViewController

@synthesize gameArray,teamArray,yourTeamArray;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self initButtons];
    
    MemoryManager *myManager = [[MemoryManager alloc] init];
    [myManager refreshData];
    gameArray = [myManager getGameArray];
    teamArray = [myManager getTeamArray];
    yourTeamArray = [myManager getYourTeamArray];
    
    gameArray = [self trimGameArray];
}

-(NSArray *)trimGameArray {
    NSMutableArray *gameMutable = [[NSMutableArray alloc] initWithArray:gameArray];
    for(int i = gameMutable.count-1;i>=0;i--)
        if(![self isYourGame:[gameMutable objectAtIndex:i]])
            [gameMutable removeObjectAtIndex:i];
    return [NSArray arrayWithArray:gameMutable];
}
                 
-(BOOL)isYourGame:(Game *)gameToTest {
    for(int i=0;i<yourTeamArray.count;i++) {
        Team *teamAtIndex = [yourTeamArray objectAtIndex:i];
        if([gameToTest.homeTeam isEqualToString:teamAtIndex.teamName] && [gameToTest.league isEqualToString:teamAtIndex.league])
            return YES;
        if([gameToTest.awayTeam isEqualToString:teamAtIndex.teamName] && [gameToTest.league isEqualToString:teamAtIndex.league])
            return YES;
    }
    return NO;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)refreshData:(id)sender
{
    MemoryManager *myManager = [[MemoryManager alloc]init];
    [myManager refreshData];
    gameArray = [myManager getGameArray];
    teamArray = [myManager getTeamArray];
    yourTeamArray = [myManager getYourTeamArray];
    gameArray = [self trimGameArray];
    [self.tableView reloadData];
}

- (IBAction)settingsButtonPressed:(id)sender
{
    UIViewController *settingsViewController = [[self storyboard] instantiateViewControllerWithIdentifier:@"SettingsNavController"];
    settingsViewController.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:settingsViewController animated:YES completion:nil];
}

- (IBAction)helpButtonPressed:(id)sender
{
    UIViewController *helpViewController = [[self storyboard] instantiateViewControllerWithIdentifier:@"HelpNavController"];
    helpViewController.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:helpViewController animated:YES completion:nil];
}

-(void)initButtons {
    UIBarButtonItem *help = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"help.png"] style:UIBarButtonItemStylePlain target:self action:@selector(helpButtonPressed:)];
    UIBarButtonItem *settings = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"gear.png"] style:UIBarButtonItemStylePlain target:self action:@selector(settingsButtonPressed:)];
    UIBarButtonItem *refresh = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"reload.png"] style:UIBarButtonItemStylePlain target:self action:@selector(refreshData:)];
    
    NSArray *buttons = [[NSMutableArray alloc] initWithObjects:settings,help,nil];
    self.navigationItem.rightBarButtonItems = buttons;
    self.navigationItem.leftBarButtonItem = refresh;
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

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return nil;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"gameCell";
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
