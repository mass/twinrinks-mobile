#import "MemoryManager.h"

@interface VC_WebView : UIViewController

@property (strong,nonatomic) UIWebView *myWebView;

-(IBAction)refreshData:id;
-(IBAction)settingsButtonPressed:id;
-(void)initButtons;

@end

@implementation VC_WebView

-(void)viewDidLoad {
    [super viewDidLoad];
    [self initButtons];
    
    _myWebView.scrollView.bounces = NO;
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    BOOL isOn = [defaults boolForKey:@"IsAutoLoginOn"];
    NSString *username = [defaults stringForKey:@"SavedUsername"];
    NSString *password = [defaults stringForKey:@"SavedPassword"];
    NSURL *url;
    
    if(isOn && username != nil && password != nil) {
        NSString *urlString = @"http://www.twinrinks.com/adulthockey/subs/subs_entry.php?subs_data1=";
        urlString = [urlString stringByAppendingString:username];
        urlString = [urlString stringByAppendingString:@"&subs_data2="];
        urlString = [urlString stringByAppendingString:password];
        url = [NSURL URLWithString:urlString];
    }
    else {
        NSString *offUrlString = @"http://www.twinrinks.com/adulthockey/subs/subs_entry.php";
        url = [NSURL URLWithString:offUrlString];
    }
    
    NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
    [_myWebView loadRequest:requestObj];
}

-(IBAction)refreshData:(id)sender {
    MemoryManager *myManager = [[MemoryManager alloc]init];
    [myManager refreshData];
}

-(IBAction)settingsButtonPressed:(id)sender {
    UIViewController *vc_Settings = [[self storyboard] instantiateViewControllerWithIdentifier:@"id_vc_settings"];
    vc_Settings.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:vc_Settings animated:YES completion:nil];
}

-(void)initButtons {
    UIBarButtonItem *settings = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"gear.png"] style:UIBarButtonItemStylePlain target:self action:@selector(settingsButtonPressed:)];
    UIBarButtonItem *refresh = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"reload.png"] style:UIBarButtonItemStylePlain target:self action:@selector(refreshData:)];
    self.navigationItem.rightBarButtonItem = settings;
    self.navigationItem.leftBarButtonItem = refresh;
}

@end